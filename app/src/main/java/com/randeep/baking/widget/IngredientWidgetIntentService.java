package com.randeep.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.randeep.baking.R;
import com.randeep.baking.data.IngredientContract;

/**
 * Created by Randeeppulp on 11/11/17.
 */

public class IngredientWidgetIntentService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext());
    }

    class ListRemoteViewFactory implements RemoteViewsFactory{

        Context mContext;
        Cursor mCursor;

        public ListRemoteViewFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

            if (mCursor != null) mCursor.close();

            mCursor = mContext.getContentResolver().query(
                    IngredientContract.IngredientEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mCursor == null) return 0;
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

            if (mCursor.getCount() != 0) {
                mCursor.moveToPosition(position);

                StringBuilder ingredient = new StringBuilder(mCursor.getString(3));
                ingredient.append("\n("+mCursor.getString(1));
                ingredient.append(" " +mCursor.getString(2)+ ")");

                rv.setTextViewText(R.id.recipe_ingredient, ingredient);
            }


            return rv;

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
