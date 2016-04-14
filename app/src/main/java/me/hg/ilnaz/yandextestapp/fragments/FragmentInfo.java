package me.hg.ilnaz.yandextestapp.fragments;

/**
 * Created by ilnaz on 13.04.16, 13:47.
 */
public class FragmentInfo {
    private int layout;
    private int menu;
    private String title;
    private boolean viewInject;
    private boolean dependencyInject;

    public boolean isDependencyInject() {
        return dependencyInject;
    }

    public FragmentInfo setDependencyInject(boolean dependencyInject) {
        this.dependencyInject = dependencyInject;
        return this;
    }

    public boolean isViewInject() {
        return viewInject;
    }

    public FragmentInfo setViewInject(boolean viewInject) {
        this.viewInject = viewInject;
        return this;
    }

    public FragmentInfo setLayout(int layout) {
        this.layout = layout;
        return this;
    }

    public FragmentInfo setMenu(int menu) {
        this.menu = menu;
        return this;
    }

    public FragmentInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getLayout() {
        return layout;
    }

    public int getMenu() {
        return menu;
    }

    public String getTitle() {
        return title;
    }
}
