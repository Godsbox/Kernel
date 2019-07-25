/*
 * Copyright (c) 2017-present 3000.com All Rights Reserved.
 */
package com.lzj.arch.bus;



/**
 * 作品内支付按钮点击事件。
 *
 * @author wsy
 */
public class WorkPayEvent {

    /**
     * 解锁事件。
     */
    public static final int WORK_UNLOCK_CHARAPTER = 1;

    /**
     * 购买商品。
     */
    public static final int WORK_GOODS_BUY = 2;

    /**
     * 闪币支付
     */
    public static final int SCOIN = 0;

    /**
     * 星星支付
     */
    public static final int STAR_COIN = 1;

    /**
     * 是解锁还是支付商品
     */
    private int type;

    /**
     * 处理结果
     */
    private boolean result;

    private boolean isRecharge;

    /**
     * 优惠券id
     */
    private int couponId = -1;

    /**
     * 是否是限时优惠
     */
    private boolean limitTime;

    private int payType = SCOIN;

    public WorkPayEvent(int type, boolean result,
                        boolean isRecharge, boolean isStarPay, int couponId, boolean isFree){
        this.type = type;
        this.result = result;
        if(isStarPay){
            this.payType = STAR_COIN;
        }
        this.couponId = couponId;
        this.isRecharge = isRecharge;
        this.limitTime = isFree;
    }

    public int getType() {
        return type;
    }

    public int getPayType(){
        return this.payType;
    }

    public boolean isResult() {
        return result;
    }

    public boolean isRecharge() {
        return isRecharge;
    }

    public int getCouponId() {
        return couponId;
    }

    public boolean isLimitTime() {
        return limitTime;
    }

    public void setLimitTime(boolean limitTime) {
        this.limitTime = limitTime;
    }
}
