package com.ef.newlead.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ef.newlead.R;
import com.ef.newlead.data.model.City;
import com.ef.newlead.util.SystemText;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by seanzhou on 8/12/16.
 * <p>
 * Adapter provides {@link City} info, allows setting filter to display city items correspondingly.
 */
public class CityAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private List<City> cityList;
    private List<String> targetCities = new LinkedList<>();
    private Context context;
    private String filter;

    public CityAdapter(Context context, List<City> cities) {
        this.context = context;
        this.cityList = cities;

        layoutInflater = LayoutInflater.from(context);
    }

    /***
     * Applies the specified filter when displaying the city info.
     *
     * @param filter
     */
    public void setFilter(String filter) {
        filter = filter.trim();
        this.filter = filter;
        if (!TextUtils.isEmpty(filter) && filter.length() > 0) {
            targetCities.clear();

            String capFilter = null;
            char firstChar = filter.charAt(0);

            // pre-check for pinyin
            String pinyinFilter = null;
            if ((firstChar >= 'A' && firstChar <= 'Z') || (firstChar >= 'a' && firstChar <= 'z')) {
                pinyinFilter = filter.toLowerCase();

                if (Character.isLowerCase(firstChar)) {
                    char prefix = Character.toUpperCase(firstChar);
                    capFilter = prefix + filter.substring(1).toLowerCase();
                } else {
                    capFilter = firstChar + filter.substring(1).toLowerCase();
                }
            }

            List<City> dstCities = new LinkedList<>();
            for (City city : cityList) {
                String pinyin = city.getPinyin();

                // the first char in pinyin will be a capital letter.
                if (city.getName().contains(filter)
                        || (pinyinFilter == null && city.getFullName().contains(filter)) // selected full name
                        || (capFilter != null && pinyin.startsWith(capFilter)) // Cap letter
                        || (pinyinFilter != null && pinyin.startsWith(pinyinFilter)) // raw pinyin
                        ) {

                    dstCities.add(city);
                }
            }

            if (dstCities.size() > 0) {
                // respecting the alpha order of PinYin
                Collections.sort(dstCities, (City lhs, City rhs) -> lhs.getPinyin().compareTo(rhs.getPinyin()));

                for (City c : dstCities) {
                    targetCities.add(c.getFullName());
                }
            }

            // It seems we can't find the required city, just show a tips.
            if (targetCities.size() == 0) {
                targetCities.add(SystemText.getSystemText(context, "city_not_found"));
            }
            update();

        } else {
            targetCities.clear();
            update();
        }
    }

    private void update() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return targetCities.size();
    }

    @Override
    public Object getItem(int position) {
        return targetCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView currentView;

        if (view == null) {
            currentView = (TextView) layoutInflater.inflate(R.layout.list_item_city, null);
        } else {
            currentView = (TextView) view;
        }

        currentView.setText(targetCities.get(position));

        return currentView;
    }
}
