package com.ef.newlead.ui.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.ef.newlead.R;
import com.ef.newlead.data.model.City;
import com.ef.newlead.ui.widget.recycleview.BasicRecyclerViewAdapter;
import com.ef.newlead.ui.widget.recycleview.ViewHolder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NewCityAdapter extends BasicRecyclerViewAdapter<City> {

    private List<City> allCities;

    public NewCityAdapter(Context context, List<City> list) {
        super(context, null);
        this.allCities = list;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_new_city;
    }

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, City item) {
        holder.setText(R.id.city_name, item.getFullName());
    }

    public List<City> setFilter(String filter) {
        filter = filter.trim();
        List<City> dstCities = null;

        if (!TextUtils.isEmpty(filter) && filter.length() > 0) {

            String capFilter = null;
            char firstChar = filter.charAt(0);
            dstCities = new LinkedList<>();

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

            for (City city : allCities) {
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
            }

        }

        return dstCities;
    }
}
