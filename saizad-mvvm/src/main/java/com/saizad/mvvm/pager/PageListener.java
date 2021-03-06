package com.saizad.mvvm.pager;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public interface PageListener<F extends BasePagerAdapterContract> {
  void onPageReady(@NonNull F page);
  void onPageSelected(@NonNull F page);
  void pagePosition(int position);
  void upcomingPage(int currentPosition, int newPosition, int newPositionVisiblePercent);
  void pageResetting(int currentPosition, int tentativePosition, int tentativePositionPercent);
  void onPageLoaded(@NotNull F page, int position);
}
