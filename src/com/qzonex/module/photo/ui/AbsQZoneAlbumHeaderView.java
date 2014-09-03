package com.qzonex.module.photo.ui;

import android.content.Context;
import android.widget.FrameLayout;
import com.qzone.model.feed.CellUserInfo;

import java.util.List;

/**
 * Qzone Android版照片列表自定义HeaderView控件API<p>
 * @author fantouchxie on 14-6-30.
 * @see <a href="http://tapd.oa.com/10054481/prong/stories/view/1010054481056089664">需求单: 【相册】相册封面和访客赞</a>
 * <p/>
 */
public abstract class AbsQZoneAlbumHeaderView extends FrameLayout {

    public AbsQZoneAlbumHeaderView(Context context) {
        super(context);
    }

    /**
     * 设置被圈出的人列表数据,如果 List.size() == 0, 或List ==null,则查看被圈的人的控件被隐藏
     * @param peopleList 被圈出的人数实体类List
     */
    public abstract void setCircledPeopleList(List<CellUserInfo> peopleList);

    /**
     * 设置头图
     * @param imageUrl 头图url,如果为null或empty则使用默认占位图
     */
    public abstract void setCoverImageUrl(String imageUrl);

    /**
     * 设置头图标题
     */
    public abstract void setCoverText(String coverText);

    /**
     * 设置访客数
     * @param visitorCount 访客数量,区间 [0, Integer.MAX_VALUE]<br>
     * 区间外的取值会被修正为 0
     */
    public abstract void setVisitorsCount(int visitorCount);

    /**
     * 设置访客按钮点击监听器
     */
    public abstract void setOnVisitorButtonClickListener(OnVisitorButtonClickListener l);

    /**
     * 设置是否高亮点赞按钮
     * @param isPraised 是否已赞
     */
    public abstract void setIsPraised(boolean isPraised);

    /**
     * 设置点赞数量
     * @param praiseCount 访客数量,区间 [0, Integer.MAX_VALUE]<br>
     * 区间外的取值会被修正为 0
     */
    public abstract void setPraiseCount(int praiseCount);

    /**
     * 设置点赞按钮点击监听器
     */
    public abstract void setOnPraiseButtonClickListener(OnPraiseButtonClickListener l);

    /**
     * 设置"上传照片"按钮点击监听器
     */
    public abstract void setOnUploadPhotoButtonClickListener(OnClickListener l);

    /**
     * 设置被圈头像点击监听器
     */
    public abstract void setOnPeopleHeadClickListener(OnPeopleHeadClickListener l);

    /**
     * 访客按钮点击监听器
     */
    public interface OnVisitorButtonClickListener {
        /**
         * @param albumHeaderView 发生点击事件的HeaderView对象
         */
        void onVisitorButtonClick(AbsQZoneAlbumHeaderView albumHeaderView);
    }

    /**
     * 点赞按钮点击监听器
     */
    public interface OnPraiseButtonClickListener {
        /**
         * 点赞按钮点击回调
         * @param albumHeaderView 发生点击事件的HeaderView对象
         * @param isPraised 是否点赞
         */
        void onPraiseButtonClick(AbsQZoneAlbumHeaderView albumHeaderView, boolean isPraised);
    }

    /**
     * 被圈头像点击监听器
     */
    public interface OnPeopleHeadClickListener{
        /**
         * 被圈头像点击回调
         * @param cellUserInfo 包含被点击的头像信息的实体类
         */
        void onPeopleHeadClick(CellUserInfo cellUserInfo);
    }

}
