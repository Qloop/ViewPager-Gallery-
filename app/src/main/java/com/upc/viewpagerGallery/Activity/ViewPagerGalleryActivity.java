package com.upc.viewpagerGallery.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.upc.viewpagerGallery.R;

import java.util.LinkedList;

/**
 * Created by Explorer on 2016/3/27.
 */
public class ViewPagerGalleryActivity extends Activity {


	private ViewPager mViewPager;
	private ImageView ivBgPic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.statusbar_color);//通知栏所需颜色
		}

		setContentView(R.layout.main_activity);
		initViews();
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	private void initViews() {
		mViewPager = (ViewPager) findViewById(R.id.vp_pager);
		ivBgPic = (ImageView) findViewById(R.id.iv_bg_pic);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setPageTransformer(true, new com.upc.viewpagerGallery.View.ZoomOutPageTransformer());
		mViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));

		MyViewPagerAdapter mAdapter = new MyViewPagerAdapter();
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOffscreenPageLimit(mAdapter.getCount());
	}




	/**
	 * Viewpager数据适配器
	 */
	class MyViewPagerAdapter extends PagerAdapter {

		//view复用
		private LinkedList<View> mViewCache = null;

		public MyViewPagerAdapter() {
			mViewCache = new LinkedList<>();
		}

		@Override
		public int getCount() {
			return 6;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}


		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ViewHolder holder = null;
			View convertView = null;
			if (mViewCache.size() == 0) {
				convertView = View.inflate(ViewPagerGalleryActivity.this, R.layout.item_viewpager, null);
				holder = new ViewHolder();
				holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_title_pic);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_exper_name);
				holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
				holder.vLine = convertView.findViewById(R.id.v_line);

				convertView.setTag(holder);
			} else {
				convertView = mViewCache.removeFirst();
				holder = (ViewHolder) convertView.getTag();
			}


			holder.ivPic.setImageResource(R.drawable.expert_default);
			holder.tvName.setText("Android工程师");
			holder.tvNum.setText("39179");

			/* 动态设置view 横线 让它和上方的文字等宽*/
			holder.tvName.measure(0, 0);
			int measuredWidth = holder.tvName.getMeasuredWidth();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(measuredWidth, 1);
			params.addRule(RelativeLayout.BELOW, R.id.tv_exper_name);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//			holder.vLine.setPadding(0, DensityUtils.dp2px(ExperimentActivity.this,1000), 0, 0);
			holder.vLine.setLayoutParams(params);


			container.addView(convertView);
			return convertView;
		}


		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			mViewCache.add((View) object);
		}

		//View复用
		public final class ViewHolder {
			public TextView tvName;
			public TextView tvNum;
			public ImageView ivPic;
			public View vLine;
		}
	}
}
