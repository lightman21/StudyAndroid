package org.android.study.ith.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import org.android.study.ith.R;
import org.android.study.ith.adapter.LoadAppAdapter;
import org.android.study.ith.entities.AppInfo;
import org.android.study.ith.util.TUtil;
import rx.Observable;

/**
 * Created by tanghao on 12/9/16.
 */

public class LoadAppActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


  @BindView(R.id.load_recyclerview)
  RecyclerView mRecylerView;

  @BindView(R.id.load_refreshlayout)
  SwipeRefreshLayout mRefreshLayout;

  private LoadAppAdapter mAdapter;
  private List<AppInfo> mData;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loadapp);
    ButterKnife.bind(this);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    mRecylerView.setLayoutManager(layoutManager);
    mData = new ArrayList<>();
    mAdapter = new LoadAppAdapter(this, mData);
    mRecylerView.setAdapter(mAdapter);

    mRefreshLayout.setColorSchemeResources(R.color.colorAccent);

    mRefreshLayout.setOnRefreshListener(this);

  }

  @Override
  public void onRefresh() {
    Observable<AppInfo> observable = TUtil.getApps(this);
    observable.subscribe(onNext -> {
      if (!mData.contains(onNext)) {
        mData.add(onNext);
        mAdapter.notifyDataSetChanged();
      }
    }, onError -> {
      mRefreshLayout.setRefreshing(false);
    }, () -> {
      mRefreshLayout.setRefreshing(false);
    });
  }
}
