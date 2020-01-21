package com.sonjinhu.bussleep.util;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by sonjh on 2017-04-08.
 */

public class ScrollListener extends RecyclerView.OnScrollListener {

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    private int draggingView = -1;

    private FloatingActionButton fab;
    private RecyclerView recycler1;
    private RecyclerView recycler2;

    public ScrollListener(FloatingActionButton fab, RecyclerView recycler1, RecyclerView recycler2) {
        this.fab = fab;
        this.recycler1 = recycler1;
        this.recycler2 = recycler2;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (recycler1 == recyclerView && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            draggingView = 1;
        } else if (recycler2 == recyclerView && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            draggingView = 2;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        initAniFab(recyclerView, dx, dy);

        if (draggingView == 1 && recyclerView == recycler1) {
            recycler2.scrollBy(dx, dy);
        } else if (draggingView == 2 && recyclerView == recycler2) {
            recycler1.scrollBy(dx, dy);
        }
    }

    private void initAniFab(RecyclerView recyclerView, int dx, int dy) {
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        if (firstVisibleItem == 0) {
            if (!controlsVisible) {
                onShow();
                controlsVisible = true;
            }
        } else {
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }
        }

        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }
    }

    private void onShow() {
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    private void onHide() {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        if (lp != null) {
            int fabMargin = lp.bottomMargin;
            fab.animate().translationY(fab.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2)).start();
        }
    }
}