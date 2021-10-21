package com.mj.taskagile.domain.common.mail;

import org.apache.commons.lang3.builder.ToStringExclude;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class SimpleMessage implements Message {

    private String to;
    private String subject;
    private String body;
    
    @EqualsAndHashCode.Exclude
    @ToStringExclude
    private String from;
}
