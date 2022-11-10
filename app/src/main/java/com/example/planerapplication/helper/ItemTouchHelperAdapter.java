package com.example.planerapplication.helper;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDelete(int position);
    void onItemCancel(int position);
    void onUpdate();
}
