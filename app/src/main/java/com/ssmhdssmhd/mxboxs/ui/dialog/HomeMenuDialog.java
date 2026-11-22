package com.ssmhdssmhd.mxboxs.ui.dialog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ssmhdssmhd.mxboxs.R;
import com.ssmhdssmhd.mxboxs.databinding.AdapterHomeMenuItemBinding;
import com.ssmhdssmhd.mxboxs.databinding.DialogHomeMenuBinding;
import com.ssmhdssmhd.mxboxs.ui.custom.SpaceItemDecoration;
import com.ssmhdssmhd.mxboxs.utils.ResUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 首页菜单键的「选项弹窗」，列出除自身之外的其它菜单键动作供直接选择。
 */
public class HomeMenuDialog extends BaseAlertDialog {

    private static final String TAG = "home_menu_dialog";
    // 菜单项按 4 列排布；去掉首项后 7 项共 2 行，仍在 maxHeight 一屏内显示完
    private static final int GRID_COUNT = 4;

    private DialogHomeMenuBinding binding;
    private Listener listener;

    public interface Listener {

        /**
         * @param index select_home_menu_key 中的下标，取值 1..N
         */
        void onHomeMenuItem(int index);
    }

    public static HomeMenuDialog create() {
        return new HomeMenuDialog();
    }

    public void show(FragmentActivity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) return;
        for (Fragment fragment : activity.getSupportFragmentManager().getFragments()) {
            if (fragment instanceof HomeMenuDialog) return;
        }
        if (activity instanceof Listener) listener = (Listener) activity;
        show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    protected ViewBinding getBinding() {
        return binding = DialogHomeMenuBinding.inflate(getLayoutInflater());
    }

    @Override
    protected MaterialAlertDialogBuilder getBuilder() {
        return builder().setView(getBinding().getRoot());
    }

    @Override
    protected void initView() {
        binding.recycler.setItemAnimator(null);
        binding.recycler.setHasFixedSize(true);
        binding.recycler.addItemDecoration(new SpaceItemDecoration(GRID_COUNT, 8));
        binding.recycler.setAdapter(new MenuAdapter());
    }

    @Override
    public void onStart() {
        super.onStart();
        setWidth(0.65f);
        binding.recycler.post(() -> {
            RecyclerView.ViewHolder holder = binding.recycler.findViewHolderForAdapterPosition(0);
            if (holder == null) binding.recycler.requestFocus();
            else holder.itemView.requestFocus();
        });
    }

    private void onItemClick(int position) {
        if (position == RecyclerView.NO_POSITION) return;
        Listener target = getListener();
        dismiss();
        if (target != null) target.onHomeMenuItem(position + 1);
    }

    private Listener getListener() {
        if (listener != null) return listener;
        return getActivity() instanceof Listener ? (Listener) getActivity() : null;
    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private final List<String> items;

        MenuAdapter() {
            items = new ArrayList<>(Arrays.asList(ResUtil.getStringArray(R.array.select_home_menu_key)));
            if (!items.isEmpty()) items.remove(0);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(AdapterHomeMenuItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.text.setText(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private final AdapterHomeMenuItemBinding binding;

            ViewHolder(@NonNull AdapterHomeMenuItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.text.setOnClickListener(view -> onItemClick(getBindingAdapterPosition()));
            }
        }
    }
}
