package com.saizad.mvvm.delegation.activity

import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvm.delegation.BaseLifecycleDelegateImp

class ActivityAppLifecycleDelegateImp<V : SaizadBaseViewModel>(
    activityAppLifeCycleCallback: ActivityAppLifeCycleCallback,
    activityCB: ActivityCB<V>,
    tag: String
) : BaseLifecycleDelegateImp<V, ActivityCB<V>>(activityAppLifeCycleCallback, activityCB, tag),
    ActivityAppLifecycleDelegate