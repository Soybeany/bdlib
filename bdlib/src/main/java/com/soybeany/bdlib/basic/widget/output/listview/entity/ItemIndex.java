package com.soybeany.bdlib.basic.widget.output.listview.entity;

/**
 * 列表项的下标
 * <br>Created by Ben on 2016/5/26.
 */
public class ItemIndex implements Cloneable {

    private static ItemIndex mInstance = new ItemIndex(); // 单例

    /**
     * 分组的位置
     */
    public int gPosition = -1;

    /**
     * 子项的位置
     */
    public int cPosition = -1;

    public ItemIndex(int gPosition, int cPosition) {
        this.gPosition = gPosition;
        this.cPosition = cPosition;
    }

    private ItemIndex() {
    }

    /**
     * 获得位置(不允许在子线程中调用)
     */
    public static ItemIndex get(int groupPosition, int childPosition) {
        mInstance.gPosition = groupPosition;
        mInstance.cPosition = childPosition;
        return mInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ItemIndex) {
            ItemIndex index = (ItemIndex) o;
            return gPosition == index.gPosition && cPosition == index.cPosition;
        }
        return super.equals(o);
    }

    @Override
    public ItemIndex clone() {
        try {
            return (ItemIndex) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new ItemIndex(gPosition, cPosition);
    }
}
