package com.ssmhdssmhd.mxboxs.bean;

import androidx.annotation.Nullable;

import com.ssmhdssmhd.mxboxs.R;
import com.ssmhdssmhd.mxboxs.impl.Diffable;
import com.ssmhdssmhd.mxboxs.utils.ResUtil;
import com.github.catvod.utils.Prefers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeButton implements Diffable<HomeButton> {

    private static final String KEY_BUTTON = "home_button";
    private static final String ALL = "0,1,2,3,4,5,6,7";

    private static List<HomeButton> buttons;

    private final int id;
    private final int resId;

    public static List<HomeButton> all() {
        if (buttons != null) return buttons;
        buttons = new ArrayList<>();
        buttons.add(new HomeButton(0, R.string.home_vod));
        buttons.add(new HomeButton(1, R.string.home_live));
        buttons.add(new HomeButton(2, R.string.home_search));
        buttons.add(new HomeButton(3, R.string.home_keep));
        buttons.add(new HomeButton(4, R.string.home_push));
        buttons.add(new HomeButton(5, R.string.home_cast));
        buttons.add(new HomeButton(6, R.string.home_history));
        buttons.add(new HomeButton(7, R.string.home_setting));
        return buttons;
    }

    public static List<HomeButton> getButtons() {
        return parse(Prefers.getString(KEY_BUTTON, ALL));
    }

    /** 判断某个 HomeButton id 是否在当前配置中启用。 */
    public static boolean isEnabled(int id) {
        for (HomeButton button : getButtons()) {
            if (button.getId() == id) return true;
        }
        return false;
    }

    public static Map<Integer, HomeButton> getMap(List<HomeButton> list) {
        Map<Integer, HomeButton> map = new LinkedHashMap<>();
        for (HomeButton button : list) map.put(button.getId(), button);
        return map;
    }

    public static void save(Map<Integer, HomeButton> map) {
        List<String> ids = new ArrayList<>();
        for (Integer id : map.keySet()) ids.add(String.valueOf(id));
        Prefers.put(KEY_BUTTON, String.join(",", ids));
    }

    public static void reset() {
        Prefers.remove(KEY_BUTTON);
    }

    private static List<HomeButton> parse(String value) {
        List<HomeButton> items = new ArrayList<>();
        Map<Integer, HomeButton> map = getMap(all());
        if (value == null || value.isEmpty()) return items;
        for (String part : value.split(",")) {
            try {
                HomeButton button = map.get(Integer.parseInt(part.trim()));
                if (button != null && !items.contains(button)) items.add(button);
            } catch (NumberFormatException ignored) {
            }
        }
        return items;
    }

    public HomeButton(int id, int resId) {
        this.id = id;
        this.resId = resId;
    }

    public int getId() {
        return id;
    }

    public int getResId() {
        return resId;
    }

    public String getName() {
        return ResUtil.getString(resId);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HomeButton it)) return false;
        return getId() == it.getId();
    }

    @Override
    public boolean isSameItem(HomeButton other) {
        return equals(other);
    }

    @Override
    public boolean isSameContent(HomeButton other) {
        return equals(other);
    }
}
