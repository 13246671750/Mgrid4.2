package com.mgrid.main;

import java.util.ArrayList;
import java.util.List;

import com.sg.uis.myfragment.TabFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class DoorActivity extends AppCompatActivity {

	TabLayout table;
	ViewPager viewPager;

	private List<Fragment> list;
	private MyAdapter adapter;
	private String[] titles = { "��ѯ", "����", "��Ȩ" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		table = (TabLayout) findViewById(R.id.tab_layout2);
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		
		list = new ArrayList<>();
        list.add(new TabFragment("Tab1"));
        list.add(new TabFragment("Tab2"));
        list.add(new TabFragment("Tab3"));
        //ViewPager��������
        adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //��
        table.setupWithViewPager(viewPager);
		// table.addTab(table.newTab().setText("��ѯ").setIcon(R.mipmap.ic_launcher));
		// table.addTab(table.newTab().setText("����"));
		// table.addTab(table.newTab().setText("��Ȩ"));

	}

	class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		// ��д���������������ÿ��Tab�ı���
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}

}