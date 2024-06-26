package com.meysam.logcollector.common.exception.messagesLoader;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class LocaleMessageSourceService {


    @Resource
    private MessageSource messageSource;

    private static LocaleMessageSourceService instance;

    @PostConstruct
    public void registerInstance() {
        instance = this;
    }

    public static LocaleMessageSourceService getInstance() {
        return instance;
    }

    public String getMessage(String code) {
        return getMessage(code, null);
    }


    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, "");
    }


    public String getMessage(String code, Object[] args, String defaultMessage) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
}
