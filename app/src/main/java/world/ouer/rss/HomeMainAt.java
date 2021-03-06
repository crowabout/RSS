package world.ouer.rss;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import world.ouer.rss.adapter.AbsRssAdapter;
import world.ouer.rss.adapter.MainPageNewsAdapter;
import world.ouer.rss.adapter.SideSubscribeSourceAdapter;
import world.ouer.rss.dao.DaoSession;
import world.ouer.rss.dao.DataQueryTools;
import world.ouer.rss.dao.RssItem;
import world.ouer.rss.dao.SourceItem;
import world.ouer.rss.net.RssAsyncService;
import world.ouer.rss.play.PlayVideoAt;
import world.ouer.rss.play.SAmPlayAudioAt;
import world.ouer.rss.play.SciPlayAudioAt;

public class HomeMainAt extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeMainAt";
    @BindView(R.id.sourceRv)
    RecyclerView sideRv;

    @BindView(R.id.RvMain)
    RecyclerView mainRv;
    private int pageIndex = 0;

    private SideSubscribeSourceAdapter sideAdapter;
    private MainPageNewsAdapter mMainPageAdapter;
    private DataQueryTools dqt;

    private boolean isLoadMore = false;
    DaoSession session;

    private Map<Long, ImageView> mMapAnimator = new HashMap<>();

    private Long selectedSid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("");
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initDqt();

        initSideRecyLv();
        initMainRecyLv();

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isSync()) {
            Log.d(TAG, "onResume: sync url");
            List<SourceItem> urls = session.getSourceItemDao().loadAll();
            new RssAsyncService(session, handler).execute(urls);
        }

    }

    private void initDqt() {
        session = ((RssApplication) getApplication()).daoSession();
        dqt = new DataQueryTools(session);
    }


    private void initSideRecyLv() {
        List<SourceItem> source = dqt.queryAllSourceItem();
        if (sideAdapter == null) {
            sideAdapter = new SideSubscribeSourceAdapter(this, source);
        }
        sideRv.setAdapter(sideAdapter);
        sideAdapter.setOnItemClickListener(new AbsRssAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v, Object item) {
                SourceItem sItem = (SourceItem) item;
                selectedSid = sItem.getId();

                pageIndex = 0;
                mMainPageAdapter.clear();
                mMainPageAdapter.update(dqt.queryRssItemBySID(pageIndex, selectedSid));

                toggleDrawer();
            }

            @Override
            public void onItemLongClick(int position) {
                Log.d(TAG, "onItemLongClick: " + position);
            }

            @Override
            public void onUpdateIconClicked(Object item, View v) {
                ImageView sideImageView = (ImageView) v;
                RotateAnimation ra = new RotateAnimation(0, 360,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                ra.setRepeatMode(RotateAnimation.INFINITE);
                ra.setRepeatCount(RotateAnimation.INFINITE);
                ra.setDuration(1000);
                sideImageView.startAnimation(ra);
                updateService((SourceItem) item);
                mMapAnimator.put(((SourceItem) item).getId(), sideImageView);

            }
        });
        sideRv.setLayoutManager(new LinearLayoutManager(this));
        sideRv.setItemAnimator(new DefaultItemAnimator());
    }

    private void initMainRecyLv() {
        List<RssItem> source = dqt.queryRssItem(pageIndex);
        mMainPageAdapter = new MainPageNewsAdapter(this, source);
        mainRv.setAdapter(mMainPageAdapter);
        mainRv.setLayoutManager(new LinearLayoutManager(this));
        mainRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int last = llm.findLastCompletelyVisibleItemPosition();
                    int count = llm.getItemCount();
                    Log.d(TAG, String.format("last:%d count:%d isLoadMore:%b", last, count, isLoadMore));
                    if (isLoadMore && (count - 1) == last) {
                        Log.i(TAG, "onScrollStateChanged: should load more");
                        pageIndex++;
                        loadNextpage(pageIndex);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isLoadMore = dy > 0;
            }
        });
        mMainPageAdapter.setOnItemClickListener(new AbsRssAdapter.OnItemClickListener<RssItem>() {
            @Override
            public void onItemClick(int position, View v, RssItem item) {
                Log.d(TAG, "_id: " + item.getId() + " position:" + position);
                String avType = RssUtils.guessTypeFromUrl(item.getEnclosure());
                Intent in = null;
                if (avType.equalsIgnoreCase("mp3")) {
                    //mp3
                    in = new Intent(HomeMainAt.this, SAmPlayAudioAt.class);

                } else if (avType.equalsIgnoreCase("mp4")) {
                    //mp4
                    in = new Intent(HomeMainAt.this, PlayVideoAt.class);
                } else if (avType.equalsIgnoreCase("txt")) {
                    //txt
                    in = new Intent(HomeMainAt.this, SciPlayAudioAt.class);
                }
                in.putExtra("rssitem", item);
                startActivity(in);
            }

            @Override
            public void onItemLongClick(int position) {

            }

            @Override
            public void onUpdateIconClicked(Object item, View imageView) {

            }
        });
    }

    private void loadNextpage(int pageIndex) {
        List<RssItem> moredata;
        if (selectedSid == null) {
            moredata = dqt.queryRssItem(pageIndex);
        } else {
            moredata = dqt.queryRssItemBySID(pageIndex, selectedSid);
        }

        if (moredata.size() == 0) {
            Toast.makeText(this, "no more data!", Toast.LENGTH_SHORT).show();
        }
        mMainPageAdapter.update(moredata);
    }

    public void addSource(View view) {
        startActivity(new Intent(this, AddRssSourceAt.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void toggleDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RssAsyncService.MESSAGE_UPDATE_NUM:
                    Toast.makeText(HomeMainAt.this, String.valueOf("store_" + msg.arg1), Toast.LENGTH_SHORT).show();
                    if (!hasLoad) {
                        notifyUpdate();
                    }
                    stopAnimate(msg.arg2);
                    break;
                case RssAsyncService.MESSAGE_UPDATE_FAIL:
                    Toast.makeText(HomeMainAt.this, String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();

                    stopAnimate(msg.arg2);
                    break;
            }
            sideAdapter.notifyDataSetChanged();
            return true;
        }
    });

    private void stopAnimate(int resourceItemId) {
        ImageView sideUpdateIcon = mMapAnimator.get(new Long(resourceItemId));
        if (sideUpdateIcon != null) {
            sideUpdateIcon.getAnimation().cancel();
            //remove from map
            mMapAnimator.remove(new Long(resourceItemId));
        }
    }


    private void notifyUpdate() {
        hasLoad = true;
        loadNextpage(pageIndex);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.downloadManager) {
            startActivity(new Intent(this, DownManagerAt.class));
        } else if (id == R.id.transcript)

        {
            startActivity(new Intent(this, TranscriptDownAt.class));
        } else if (id == R.id.setting)

        {
            startActivity(new Intent(this, SettingAt.class));
        }
        return super.

                onOptionsItemSelected(item);

    }

    private void updateService(SourceItem item) {
        List l = new ArrayList(1);
        l.add(item);
        new RssAsyncService(session, handler).execute(l);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Log.d(TAG, "onNavigationItemSelected: " + item.getMenuInfo());

        int id = item.getItemId();
        if (id == R.id.nav_camera) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isSync() {
        return dqt.rssItemCount() == 0;
    }


    boolean hasLoad = false;
}
