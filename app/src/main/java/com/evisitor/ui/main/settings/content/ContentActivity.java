package com.evisitor.ui.main.settings.content;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.evisitor.R;
import com.evisitor.ViewModelProviderFactory;
import com.evisitor.databinding.ActivityContentBinding;
import com.evisitor.ui.base.BaseActivity;
import com.evisitor.util.AppConstants;

public class ContentActivity extends BaseActivity<ActivityContentBinding, ContentViewModel> {

    public static Intent newIntent(Context context) {
        return new Intent(context, ContentActivity.class);
    }

    @Override
    public int getBindingVariable() {
        return com.evisitor.BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_content;
    }

    @Override
    public ContentViewModel getViewModel() {
        return new ViewModelProvider(this, ViewModelProviderFactory.getInstanceM()).get(ContentViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel.setNavigator(this);

        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(v -> onBackPressed());
        setIntentData(getIntent());
    }

    private void setIntentData(Intent intent) {
        TextView tvTitle = findViewById(R.id.tv_title);

        if (intent.hasExtra("From")) {
            String from = intent.getStringExtra("From");

            switch (from != null ? from : "") {
                case AppConstants.ACTIVITY_ABOUT_US:
                    tvTitle.setText(R.string.title_about_us);
                    break;

                case AppConstants.ACTIVITY_PRIVACY:
                    tvTitle.setText(R.string.title_privacy_policy);
                    break;
            }

            String data = "A basic privacy policy outlines your website’s relationship with users’ personal information.\n" +
                    "\n" +
                    "To succeed online and avoid legal turmoil, your website needs a privacy policy agreement. The first step to creating a compliant and comprehensive privacy policy is understanding exactly what that is.\n" +
                    "\n" +
                    "Privacy Policy Definition\n" +
                    "A privacy policy is a legal document that informs your site’s users about how you collect and handle their personal information. You may also hear privacy policies referred to by the following names:\n" +
                    "\n" +
                    "Privacy notice\n" +
                    "Privacy policy statement\n" +
                    "Privacy page\n" +
                    "Privacy clause\n" +
                    "Privacy agreement\n" +
                    "A general privacy policy explains a platform’s interactions with the personal information and personally identifiable information (PII) of its users. PII is information that can be used by itself, or combined with other information, to identify an individual.";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getViewDataBinding().tvContent.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
            } else {
                getViewDataBinding().tvContent.setText(Html.fromHtml(data));
            }
            /*mViewModel.getContent(from).observe(this, contentResponse -> {
                String data;
                switch (from != null ? from : "") {
                    case AppConstants.ACTIVITY_ABOUT_US:
                        data = contentResponse.getData().getAbout_us_content();
                        break;

                    case AppConstants.ACTIVITY_PRIVACY:
                        data = contentResponse.getData().getPrivacy_policy_content();
                        break;

                    default:
                        data = "";
                        break;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    getViewDataBinding().tvContent.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    getViewDataBinding().tvContent.setText(Html.fromHtml(data));
                }
            });*/
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntentData(intent);
    }
}
