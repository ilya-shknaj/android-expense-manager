//package by.gravity.expensemanager.view;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.RectF;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewConfiguration;
//import android.widget.AbsListView;
//import android.widget.ExpandableListAdapter;
//import android.widget.ExpandableListView;
//import by.gravity.common.utils.ReflectionUtils;
//import by.gravity.expensemanager.adapter.IPinnedHeaderAdapter;
//
//public class PinnedHeaderExpListView extends ExpandableListView {
//
//	private ExpandableListAdapter mAdapter;
//	private View mHeaderView;
//	private boolean mHeaderViewVisible;
//
//	private int mHeaderViewWidth;
//
//	private int mHeaderViewHeight;
//
//	private Runnable mOnClickAction;
//
//	private int mFloatingGroupPackedPosition;
//
//	private boolean mShouldPositionSelector;
//
//	public PinnedHeaderExpListView(Context context) {
//		super(context);
//	}
//
//	public PinnedHeaderExpListView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public PinnedHeaderExpListView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	public void setPinnedHeaderView(View view) {
//		mHeaderView = view;
//
//		// Disable vertical fading when the pinned header is present
//		// TODO change ListView to allow separate measures for top and bottom
//		// fading edge;
//		// in this particular case we would like to disable the top, but not the
//		// bottom edge.
//		if (mHeaderView != null) {
//			setFadingEdgeLength(0);
//		}
//
//		mOnClickAction = new Runnable() {
//
//			@Override
//			public void run() {
//				if (isGroupExpanded(mFloatingGroupPackedPosition)) {
//					collapseGroup(mFloatingGroupPackedPosition);
//				} else {
//					expandGroup(mFloatingGroupPackedPosition);
//				}
//				setSelectedGroup(mFloatingGroupPackedPosition);
//			}
//
//		};
//
//		requestLayout();
//	}
//
//	@Override
//	public void setAdapter(ExpandableListAdapter adapter) {
//		super.setAdapter(adapter);
//		mAdapter = (ExpandableListAdapter) adapter;
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		if (mHeaderView != null) {
//			measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
//			mHeaderViewWidth = mHeaderView.getMeasuredWidth();
//			mHeaderViewHeight = mHeaderView.getMeasuredHeight();
//		}
//	}
//
//	@Override
//	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//		super.onLayout(changed, left, top, right, bottom);
//		if (mHeaderView != null) {
//			mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
//			configureHeaderView(getFirstVisiblePosition());
//		}
//	}
//
//	/**
//	 * animating header pushing
//	 * 
//	 * @param position
//	 */
//	public void configureHeaderView(int position) {
//		final int group = getPackedPositionGroup(getExpandableListPosition(position));
//
//		if (mHeaderView == null) {
//			return;
//		}
//
//		mFloatingGroupPackedPosition = getPackedPositionGroup(getExpandableListPosition(position));
//
//		mHeaderView.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View header) {
//				postDelayed(mOnClickAction, ViewConfiguration.getPressedStateDuration());
//			}
//		});
//
//		int state, nextSectionPosition = getFlatListPosition(getPackedPositionForGroup(group + 1));
//
//		if (mAdapter.getGroupCount() == 0) {
//			state = IPinnedHeaderAdapter.PINNED_HEADER_GONE;
//		} else if (position < 0) {
//			state = IPinnedHeaderAdapter.PINNED_HEADER_GONE;
//		} else if (nextSectionPosition != -1 && position == nextSectionPosition - 1) {
//			state = IPinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
//		} else
//			state = IPinnedHeaderAdapter.PINNED_HEADER_VISIBLE;
//
//		switch (state) {
//		case IPinnedHeaderAdapter.PINNED_HEADER_GONE: {
//			mHeaderViewVisible = false;
//			break;
//		}
//
//		case IPinnedHeaderAdapter.PINNED_HEADER_VISIBLE: {
//			mAdapter.configurePinnedHeader(mHeaderView, isGroupExpanded(group), group);
//			if (mHeaderView.getTop() != 0) {
//				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
//			}
//			mHeaderViewVisible = true;
//			break;
//		}
//
//		case IPinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
//			View firstView = getChildAt(0);
//			if (firstView == null) {
//				if (mHeaderView.getTop() != 0) {
//					mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
//				}
//				// mHeaderViewVisible = true;
//				// break;
//			}
//			int bottom = firstView == null ? mHeaderView.getBottom() : firstView.getBottom();
//			int headerHeight = mHeaderView.getHeight();
//			int y;
//			if (bottom < headerHeight) {
//				y = (bottom - headerHeight);
//			} else {
//				y = 0;
//			}
//			mAdapter.configurePinnedHeader(mHeaderView, isGroupExpanded(group), group);
//			// выползание
//			if (mHeaderView.getTop() != y) {
//				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
//			}
//			mHeaderViewVisible = true;
//
//			break;
//		}
//		}
//
//	}
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		final int action = ev.getAction() & MotionEvent.ACTION_MASK;
//
//		final int screenCoords[] = new int[2];
//		getLocationInWindow(screenCoords);
//		final RectF floatingGroupRect = new RectF(screenCoords[0] + mHeaderView.getLeft(), screenCoords[1] + mHeaderView.getTop(), screenCoords[0]
//				+ mHeaderView.getRight(), screenCoords[1] + mHeaderView.getBottom());
//
//		if (floatingGroupRect.contains(ev.getRawX(), ev.getRawY())) {
//			switch (action) {
//			case MotionEvent.ACTION_DOWN:
//				mShouldPositionSelector = true;
//				break;
//			case MotionEvent.ACTION_UP:
//				positionSelectorOnFloatingGroup();
//				setPressed(true);
//				if (mHeaderView != null) {
//					mHeaderView.setPressed(true);
//				}
//				break;
//			}
//			if (mHeaderView.dispatchTouchEvent(ev)) {
//				onInterceptTouchEvent(ev);
//				return true;
//			}
//		}
//
//		return super.dispatchTouchEvent(ev);
//	}
//
//	private void positionSelectorOnFloatingGroup() {
//		if (mShouldPositionSelector && mHeaderView != null) {
//			final int floatingGroupFlatPosition = getFlatListPosition(getPackedPositionForGroup(mFloatingGroupPackedPosition));
//			ReflectionUtils.invokeMethod(AbsListView.class, "positionSelector", new Class<?>[] { int.class, View.class },
//					PinnedHeaderExpListView.this, floatingGroupFlatPosition, mHeaderView);
//			invalidate();
//		}
//		mShouldPositionSelector = false;
//	}
//
//	@Override
//	protected void dispatchDraw(Canvas canvas) {
//		super.dispatchDraw(canvas);
//		if (mHeaderViewVisible) {
//			drawChild(canvas, mHeaderView, getDrawingTime());
//		}
//	}
//
//}