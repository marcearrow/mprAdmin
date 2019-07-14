package com.bumptech.glide.manager;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A {@link com.bumptech.glide.manager.Lifecycle} implementation for tracking and notifying
 * listeners of {@link android.app.Fragment} and {@link android.app.Activity} lifecycle events.
 */
class ActivityFragmentLifecycle implements Lifecycle {

  private final Set<LifecycleListener> lifecycleListeners =
      Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());
  private boolean isStarted;
  private boolean isDestroyed;

/**
 * Adds the given listener to the list of listeners to be notified on each lifecycle event.
 *
 * <p> The latest lifecycle event will
