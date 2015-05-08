package com.github.clans.fab.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.github.fab.sample.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

public class FloatingMenusActivity extends ActionBarActivity {

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;

    private FloatingActionButton fab12;
    private FloatingActionButton fab22;
    private FloatingActionButton fab32;

    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_menus_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionMenu menu1 = (FloatingActionMenu) findViewById(R.id.menu1);
        final FloatingActionMenu menu2 = (FloatingActionMenu) findViewById(R.id.menu2);
        final FloatingActionMenu menu3 = (FloatingActionMenu) findViewById(R.id.menu3);
        FloatingActionMenu menu4 = (FloatingActionMenu) findViewById(R.id.menu4);
        FloatingActionMenu menuDown = (FloatingActionMenu) findViewById(R.id.menu_down);

        FloatingActionButton fab = new FloatingActionButton(this);
        fab.setButtonSize(FloatingActionButton.SIZE_MINI);
        fab.setLabelText("Programmatically added button");
        fab.setImageResource(R.drawable.ic_edit);
        menu1.addMenuButton(fab);

        ContextThemeWrapper context = new ContextThemeWrapper(this, R.style.MenuButtonsStyle);
        FloatingActionButton fab2 = new FloatingActionButton(context);
        fab2.setLabelText("Programmatically added button");
        fab2.setImageResource(R.drawable.ic_edit);
        menu2.addMenuButton(fab2);

        menus.add(menuDown);
        menus.add(menu1);
        menus.add(menu2);
        menus.add(menu3);
        menus.add(menu4);

        menuDown.hideMenuButton(false);
        menu1.hideMenuButton(false);
        menu2.hideMenuButton(false);
        menu3.hideMenuButton(false);
        menu4.hideMenuButton(false);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }


        menu1.setClosedOnTouchOutside(true);

        menu4.setIconAnimated(false);

        menu2.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                String text = "";
                if (opened) {
                    text = "Menu opened";
                } else {
                    text = "Menu closed";
                }
                Toast.makeText(FloatingMenusActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);

        fab12 = (FloatingActionButton) findViewById(R.id.fab12);
        fab22 = (FloatingActionButton) findViewById(R.id.fab22);
        fab32 = (FloatingActionButton) findViewById(R.id.fab32);

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);

        fab12.setOnClickListener(clickListener);
        fab22.setOnClickListener(clickListener);
        fab32.setOnClickListener(clickListener);

        final FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fabEdit.setShowAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_up));
        fabEdit.setHideAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_down));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabEdit.show(true);
            }
        }, delay + 150);

        findViewById(R.id.fab_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FloatingMenusActivity.this, RecyclerViewActivity.class));
            }
        });

        createCustomAnimation();
    }

    private void createCustomAnimation() {
        final FloatingActionMenu menu3 = (FloatingActionMenu) findViewById(R.id.menu3);

        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menu3.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menu3.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menu3.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menu3.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menu3.getMenuIconView().setImageResource(menu3.isOpened()
                        ? R.drawable.ic_close : R.drawable.ic_star);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menu3.setIconToggleAnimatorSet(set);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = "";

            switch (v.getId()) {
                case R.id.fab1:
                    text = fab1.getLabelText();
                    break;
                case R.id.fab2:
                    text = fab2.getLabelText();
                    break;
                case R.id.fab3:
                    text = fab3.getLabelText();
                    break;
                case R.id.fab12:
                    text = fab12.getLabelText();
                    break;
                case R.id.fab22:
                    text = fab22.getLabelText();
                    break;
                case R.id.fab32:
                    text = fab32.getLabelText();
                    break;
            }

            Toast.makeText(FloatingMenusActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    };
}
