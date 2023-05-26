package com.snabeel.dappethr.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.snabeel.dappethr.R;

public class OnBoardingAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public OnBoardingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_adapter, container, false);

        TextView title = view.findViewById(R.id.on_boarding_title_tv);
        TextView desc = view.findViewById(R.id.on_boarding_desc_tv);
        ImageView image = view.findViewById(R.id.slide_images);

        if(position == 0){
            image.setImageResource(R.mipmap.slide1);
            title.setText(R.string.welcome_to_eth_wallet);
            desc.setText(R.string.welcome_to_our_ethereum);
        }else if (position == 1){
//            Glide.with(context).asGif().load(R.drawable.slide2gif).into(image);
            image.setImageResource(R.mipmap.slide2);
            title.setText(R.string.manage_your_digital_assets);
            desc.setText(R.string.send_and_receive);
        }else{
//            Glide.with(context).load(R.mipmap.slide3).into(image);
            image.setImageResource(R.mipmap.slide3);
            title.setText(R.string.welcome_to_web3);
            desc.setText(R.string.web3_desc);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
