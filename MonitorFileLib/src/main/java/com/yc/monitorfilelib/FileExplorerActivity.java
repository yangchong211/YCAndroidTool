package com.yc.monitorfilelib;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayDeque;

/**
 * description: 文件管理
 * @author  杨充
 * @since   2021/8/11
 */
public class FileExplorerActivity extends AppCompatActivity {

    private final ArrayDeque<Fragment> mFragments = new ArrayDeque<>();
    private static final String TAG = "FileExplorerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showContent(FileExplorerFragment.class);
    }

    public void showContent(Class<? extends Fragment> target) {
        showContent(target, null);
    }

    /**
     * 添加fragment
     * @param target                        target对象
     * @param bundle                        bundle数据
     */
    public void showContent(Class<? extends Fragment> target, Bundle bundle) {
        try {
            Fragment fragment = target.newInstance();
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(android.R.id.content, fragment);
            mFragments.push(fragment);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
        } catch (InstantiationException exception) {
            FileExplorerUtils.logError(TAG + exception.toString());
        } catch (IllegalAccessException exception) {
            FileExplorerUtils.logError(TAG + exception.toString());
        }
    }

    /**
     * 处理fragment返回键逻辑
     */
    public void onBackPressed() {
        if (!mFragments.isEmpty()) {
            Fragment fragment = mFragments.getFirst();
            if (fragment!=null){
                mFragments.removeFirst();
            }
            super.onBackPressed();
            if (mFragments.isEmpty()) {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 回退fragment任务栈操作
     * @param fragment                  fragment
     */
    public void doBack(Fragment fragment) {
        if (mFragments.contains(fragment)) {
            mFragments.remove(fragment);
            FragmentManager fm = getSupportFragmentManager();
            //回退fragment操作
            fm.popBackStack();
            if (mFragments.isEmpty()) {
                //如果fragment栈为空，则直接关闭宿主activity
                finish();
            }
        }
    }

}
