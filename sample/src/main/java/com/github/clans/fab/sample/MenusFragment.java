package com.github.clans.fab.sample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.fab.sample.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MenusFragment extends Fragment {

    public class FabMenuItem {
        public int textId = -1;
        public int iconId = -1;
        @SuppressWarnings("WeakerAccess") public int itemId = -1;

        @SuppressWarnings("WeakerAccess") public FabMenuItem(int itemId, int textId, int iconId) {
            this.itemId = itemId;
            this.textId = textId;
            this.iconId = iconId;
        }
    };

    public FabMenuItem FAB_MENU_ITEM_ADD_TO_AUTOTEST = new FabMenuItem(R.id.fabMenuItem_test_common_addToAutoTest,
            R.string.common_test_Add_to_AutoTest, R.drawable.ic_common_settings_white_36dp);

    public FabMenuItem FAB_MENU_ITEM_BROWSE = new FabMenuItem(R.id.fabMenuItem_test_common_browse,
            R.string.common_test_Browse, R.drawable.ic_common_settings_white_36dp);

    public FabMenuItem FAB_MENU_ITEM_MORE = new FabMenuItem(R.id.fabMenuItem_test_menu_more,
            R.string.common_test_More, R.drawable.ic_common_more_horiz_white_36dp);

    private FloatingActionMenu menuRed;
    private FloatingActionMenu menuYellow;
    private FloatingActionMenu menuGreen;
    private FloatingActionMenu menuBlue;
    private FloatingActionMenu menuFabPrimaryMenu;
    private FloatingActionMenu menuDown;
    private FloatingActionMenu menuLabelsRight;

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;

    private FloatingActionButton fabEdit;

    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menus_fragment, container, false);
    }

    private void styleFab(FloatingActionButton fab) {
        fab.setColorNormal(getContext().getColor(R.color.fab_color));
        fab.setColorPressed(getContext().getColor(R.color.fab_color));
        fab.setButtonSize(FloatingActionButton.SIZE_MINI);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuRed = (FloatingActionMenu) view.findViewById(R.id.menu_red);
        menuYellow = (FloatingActionMenu) view.findViewById(R.id.menu_yellow);
        menuGreen = (FloatingActionMenu) view.findViewById(R.id.menu_green);
        menuBlue = (FloatingActionMenu) view.findViewById(R.id.menu_blue);
        menuFabPrimaryMenu = (FloatingActionMenu) view.findViewById(R.id.fab_primary_menu);
        menuDown = (FloatingActionMenu) view.findViewById(R.id.menu_down);
        menuLabelsRight = (FloatingActionMenu) view.findViewById(R.id.menu_labels_right);

        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);

        final FloatingActionButton programFab1 = new FloatingActionButton(getActivity());
        programFab1.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab1.setLabelText(getString(R.string.lorem_ipsum));
        programFab1.setImageResource(R.drawable.ic_edit);
        menuRed.addMenuButton(programFab1);
        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programFab1.setLabelColors(ContextCompat.getColor(getActivity(), R.color.grey),
                        ContextCompat.getColor(getActivity(), R.color.light_grey),
                        ContextCompat.getColor(getActivity(), R.color.white_transparent));
                programFab1.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.black));
            }
        });

        ContextThemeWrapper context = new ContextThemeWrapper(getActivity(), R.style.MenuButtonsStyle);
        FloatingActionButton programFab2 = new FloatingActionButton(context);
        programFab2.setLabelText("Programmatically added button");
        programFab2.setImageResource(R.drawable.ic_edit);
        menuYellow.addMenuButton(programFab2);

        fab1.setEnabled(false);
        menuRed.setClosedOnTouchOutside(true);
        menuBlue.setIconAnimated(false);

        menuDown.hideMenuButton(false);
        menuRed.hideMenuButton(false);
        menuYellow.hideMenuButton(false);
        menuGreen.hideMenuButton(false);
        menuBlue.hideMenuButton(false);

        // Initialize stand-in Andromeda FAM:
        List<FabMenuItem> primaryFabContents = new ArrayList<>();
        primaryFabContents.add(FAB_MENU_ITEM_ADD_TO_AUTOTEST);
        primaryFabContents.add(FAB_MENU_ITEM_BROWSE);
        primaryFabContents.add(FAB_MENU_ITEM_MORE);

        for (FabMenuItem fabMenuItem : primaryFabContents) {
            FloatingActionButton fab = new FloatingActionButton(getContext());
            fab.setImageDrawable(getContext().getDrawable(fabMenuItem.iconId));
            fab.setLabelText(getContext().getString(fabMenuItem.textId));
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

            fab.setLayoutParams(lp);
            fab.setId(fabMenuItem.itemId);
            styleFab(fab);
            menuFabPrimaryMenu.addMenuButton(fab);
        }

        menuFabPrimaryMenu.setOpenIcon(getContext().getDrawable(R.drawable.ic_star));

        menuFabPrimaryMenu.setVisibility(View.VISIBLE);

        menuLabelsRight.hideMenuButton(false);

        fabEdit = (FloatingActionButton) view.findViewById(R.id.fab_edit);
        fabEdit.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up));
        fabEdit.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menus.add(menuDown);
        menus.add(menuRed);
        menus.add(menuYellow);
        menus.add(menuGreen);
        menus.add(menuBlue);
        menus.add(menuFabPrimaryMenu);
        menus.add(menuLabelsRight);

        menuYellow.setOnMenuToggleFinishedListener(new FloatingActionMenu.OnMenuToggleFinishedListener() {
            @Override
            public void onMenuToggleFinished(boolean opened) {
                String text;
                if (opened) {
                    text = "Menu opened";
                } else {
                    text = "Menu closed";
                }
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabEdit.show(true);
            }
        }, delay + 150);

        menuRed.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuRed.isOpened()) {
                    Toast.makeText(getActivity(), menuRed.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                menuRed.toggle(true);
            }
        });

        // createCustomAnimation(); // Netscout change: disable this so we can test with the
                                    // green menu.
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuGreen.getMenuIconView().setImageResource(menuGreen.isOpened()
                        ? R.drawable.ic_close : R.drawable.ic_star);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menuGreen.setIconToggleAnimatorSet(set);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    break;
                case R.id.fab2:
                    fab2.setVisibility(View.GONE);
                    break;
                case R.id.fab3:
                    fab2.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}
