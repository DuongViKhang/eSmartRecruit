package com.example.eSmartRecruit.services;

import com.example.eSmartRecruit.models.Application;


public interface IApplicationService {
    public String apply(Application applications);
    public String update(Application applications, Integer id);
}
