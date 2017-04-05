package com.kirilv.android.splitme;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.kirilv.android.splitme.model.Result;
import com.kirilv.android.splitme.model.Section;
import com.kirilv.android.splitme.model.User;

public class ReportActivity extends SwipeActivity {

	private static final DecimalFormat formatter = new DecimalFormat("0.00");
	private final SparseArray<Result> totalMap = new SparseArray<Result>();
	private File file;

	private static int[] COLORS = new int[] { Color.parseColor("#3187a3"),
			Color.parseColor("#555555"), Color.parseColor("#2e9419"),
			Color.parseColor("#74bdd4"), Color.parseColor("#e34d1b"),
			Color.parseColor("#888888"), Color.parseColor("#7acf63"),
			Color.parseColor("#32b4e3"), Color.parseColor("#e6e645"),
			Color.parseColor("#BBBBBB"), Color.parseColor("#aef09c"),
			Color.parseColor("#f0bc9c"), Color.parseColor("#cf0003"),
			Color.parseColor("#8bf018"), Color.parseColor("#9218f0"),
			Color.parseColor("#313ed4"), Color.parseColor("#859920"),
			Color.parseColor("#48754b"), Color.parseColor("#734766"),
			Color.parseColor("#fa467c"), Color.parseColor("#de6c43") };

	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	/** The chart view that displays the data. */
	private GraphicalView mChartView;

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		mSeries = (CategorySeries) savedState.getSerializable("current_series");
		mRenderer = (DefaultRenderer) savedState
				.getSerializable("current_renderer");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("current_series", mSeries);
		outState.putSerializable("current_renderer", mRenderer);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getResources().getString(R.string.lbl_title_report));

		mRenderer.setStartAngle(180);
		mRenderer.setDisplayValues(true);
		mRenderer.setShowLegend(true);
		mRenderer.setLegendTextSize(getResources().getInteger(R.integer.report_legend_text_size));
		mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setLabelsTextSize(getResources().getInteger(R.integer.report_label_text_size));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
			mRenderer.setClickEnabled(true);
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection != null) {
						for (int i = 0; i < mSeries.getItemCount(); i++) {
							mRenderer.getSeriesRendererAt(i).setHighlighted(
									i == seriesSelection.getPointIndex());
						}
						mChartView.repaint();
						showToast(mSeries.getCategory(seriesSelection
								.getPointIndex())
								+ ": "
								+ formatter.format(seriesSelection.getValue()));
					}
				}
			});
			layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

		final SparseArray<Section> sectionMap = ApplicationHolder.getInstance().getSectionsMap();
		final int sectionsSize = sectionMap.size();
		for (int i = 0; i < sectionsSize; i++) {
			Section section = sectionMap.get(i);
			final float sectionAmount = section.getAmount();
			List<User> list = section.getUserList();
			for (User u : list) {
				final float amount = CalculationEngine.getPrice(u.getPercentage(), sectionAmount);
				Result res = totalMap.get(u.getIndex());
				if (res != null) {
					res.setAmount(res.getAmount() + amount);
				} else {
					res = new Result();
					res.setIndex(u.getIndex());
					res.setAmount(amount);
					res.setName(u.getName());
				}
				totalMap.append(u.getIndex(), res);
			}
		}
		mSeries.clear();
		mRenderer.removeAllRenderers();
		for (int i = 0; i < totalMap.size(); i++) {
			Result result = totalMap.get(i);
			if (result.getAmount() > 0) {
				mSeries.add(result.getName(), result.getAmount());
				SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
				renderer.setChartValuesFormat(formatter);
				renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
						% COLORS.length]);
				mRenderer.addSeriesRenderer(renderer);
			}
		}
		mChartView.setOnTouchListener(swipeDetector);
		mChartView.repaint();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_report, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			cancelToast();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_share:
			cancelToast();
			exportReport();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (file != null) {
			file.delete();
		}
	}

	private void exportReport() {
		StringBuffer reportString = new StringBuffer();
		reportString.append("\""
				+ getResources().getString(R.string.export_name) + "\",\""
				+ getResources().getString(R.string.export_price) + "\"");
		for (int i = 0; i < totalMap.size(); i++) {
			Result res = totalMap.get(i);
			String dataString = "\"" + res.getName() + "\",\""
					+ formatter.format(res.getAmount()) + "\"";
			reportString.append("\n");
			reportString.append(dataString);
		}

		File root = Environment.getExternalStorageDirectory();
		if (root.canWrite()) {
			Writer writer = null;
			try {
				File dir = new File(root.getAbsolutePath() + "/PersonData");
				dir.mkdirs();
				file = new File(dir, "SplitMe.csv");
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file), "cp1251"));
				writer.write(reportString.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
			}
		}

		if (file != null) {
			Uri uri = Uri.fromFile(file);
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Data Export");
			sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
			sendIntent.setType("text/html");
			startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.lbl_title_report_export)));
		}
	}

	@Override
	public void getNextActivity() {
	}
}