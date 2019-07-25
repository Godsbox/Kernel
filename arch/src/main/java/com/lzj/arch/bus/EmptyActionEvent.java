package com.lzj.arch.bus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmptyActionEvent {
    /**
     * 事件参数
     */
    private String event;
}
