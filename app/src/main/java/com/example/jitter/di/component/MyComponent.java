package com.example.jitter.di.component;

import com.example.jitter.di.module.MyNetworkModule;
import com.example.jitter.di.scope.PerApp;
import com.example.jitter.fragment.TweetsFragment;

import dagger.Component;

@PerApp
@Component(modules = MyNetworkModule.class)
public interface MyComponent {
    void inject(TweetsFragment fragment);
}
