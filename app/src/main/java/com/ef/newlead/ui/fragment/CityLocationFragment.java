package com.ef.newlead.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.newlead.R;
import com.ef.newlead.data.model.City;
import com.ef.newlead.ui.adapter.CityAdapter;
import com.ef.newlead.util.FileUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by seanzhou on 8/12/16.
 * <p>
 * Fragment provides user the ability to select city or just locate the position automatically.
 */
public class CityLocationFragment extends BaseFragment implements TextWatcher, AdapterView.OnItemClickListener {

    @BindView(R.id.cancel_input)
    ImageView cancel;

    @BindView(R.id.editText)
    EditText input;

    @BindView(R.id.city_location)
    TextView location;

    @BindView(R.id.listView)
    ListView cityListView;

    @BindView(R.id.button)
    Button submit;

    private CityAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_location;
    }

    @Override
    public void initView() {
        input.addTextChangedListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Type type = new TypeToken<List<City>>() {
        }.getType();

        List<City> cities = FileUtils.readObjectFromAssertFile(this.getContext(), "cities.json", type);

        adapter = new CityAdapter(this.getActivity(), cities);
        cityListView.setAdapter(adapter);

        cityListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        input.setText((String)adapter.getItem(position));

        submit.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.city_location)
    void onLocate() {

    }

    @OnClick(R.id.cancel_input)
    void onCancelInput() {
        input.setText("");
    }

    @OnClick(R.id.button)
    void onSubmit() {
        Toast.makeText(getActivity(), "submit button clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        // update list
        String filter = s.toString();
        submit.setVisibility(filter.isEmpty() ? View.VISIBLE : View.GONE);

        adapter.setFilter(filter);
    }
}
