package world.ouer.rss;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

public class HomeMainAt extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeMainAt";
    @BindView(R.id.sourceRv)
    RecyclerView sourceRv;

    @BindView(R.id.RvMain)
    RecyclerView rvMain;
    private int pageIndex = 0;

    private SideSubscribeSourceAdapter sideAdapter;
    private MainPageNewsAdapter mMainPageAdapter;
    private DataQueryTools dqt;

    private boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("");
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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


    private void initDqt() {
        DaoSession session = ((RssApplication) getApplication()).daoSession();
        dqt = new DataQueryTools(session);

    }

    private void initSideRecyLv() {

        List<SourceItem> source = dqt.queryAllSourceItem();
        if (sideAdapter == null) {
            sideAdapter = new SideSubscribeSourceAdapter(this, source);
        }
        sourceRv.setAdapter(sideAdapter);
        sourceRv.setLayoutManager(new LinearLayoutManager(this));
        sourceRv.setItemAnimator(new DefaultItemAnimator());
    }

    private void initMainRecyLv() {
        List<RssItem> source = dqt.queryRssItem(pageIndex);
        mMainPageAdapter = new MainPageNewsAdapter(this, source);
        rvMain.setAdapter(mMainPageAdapter);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            public void onItemClick(int position, View v,RssItem item) {
                Log.d(TAG, "_id: "+item.getId()+" position:"+position);

            }

            @Override
            public void onItemLongClick(int position) {

            }
        });


    }

    private void loadNextpage(int pageIndex) {
        List<RssItem> moredata = dqt.queryRssItem(pageIndex);
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
                    Toast.makeText(HomeMainAt.this, String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
                    if(!hasLoad){
                       notifyUpdate();
                    }
                    break;
                case RssAsyncService.MESSAGE_UPDATE_FAIL:
                    Toast.makeText(HomeMainAt.this, String.valueOf(msg.obj), Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });


    boolean hasLoad=false;
    private void notifyUpdate(){
        hasLoad=true;
        loadNextpage(pageIndex);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.sync) {
            if (isSync()) {
                DaoSession session = ((RssApplication) getApplication()).daoSession();
                List<SourceItem> urls = session.getSourceItemDao().loadAll();
                new RssAsyncService(session, handler).execute(urls);
            }
            return true;
        } else if (id == R.id.downloadManager) {
            startActivity(new Intent(this, DownManagerAt.class));
        }else if(id==R.id.test){
            startActivity(new Intent(this, AddRssSourceAt.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isSync() {

        SourceItem item = dqt.querySourceItemLastAccessTime();
        String lastUpdateTime = item.getLastTimeAccess();
        if (TextUtils.isEmpty(lastUpdateTime)) {
            return true;
        }

        lastUpdateTime = lastUpdateTime.replaceAll("[0-9]{2}:[0-9]{2}:[0-9]{2}\\s-[0-9]{4}", "").trim();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE,dd LLL yyyy", Locale.US);
        try {
            Date date = sdf.parse(lastUpdateTime);
            Calendar nowCal = Calendar.getInstance();

            Calendar cc = Calendar.getInstance();
            cc.setTime(date);
            if (nowCal.after(cc)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
