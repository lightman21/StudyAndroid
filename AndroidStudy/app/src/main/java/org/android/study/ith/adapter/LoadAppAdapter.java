package org.android.study.ith.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import java.util.List;
import org.android.study.ith.R;
import org.android.study.ith.entities.AppInfo;

/**
 * Created by tanghao on 12/9/16.
 */
public class LoadAppAdapter extends RecyclerView.Adapter<LoadAppAdapter.ViewHolder> {

  private List<AppInfo> mData;
  private Context context;

  public LoadAppAdapter(Context context, List<AppInfo> data) {
    this.context = context;
    this.mData = data;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loadapp, parent, false);
    ViewHolder vh = new ViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    AppInfo info = mData.get(position);

    Glide.with(context).load(0).placeholder(info.getIconDrawable()).centerCrop().dontAnimate().into(holder.iconIv);

    holder.lableTv.setText(info.getName());
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }


  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.info_text)
    TextView lableTv;
    @BindView(R.id.info_icon)
    ImageView iconIv;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
