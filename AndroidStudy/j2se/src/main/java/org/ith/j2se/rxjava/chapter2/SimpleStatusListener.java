package org.ith.j2se.rxjava.chapter2;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

/**
 * Created by tanghao on 12/22/16.
 */

public abstract class SimpleStatusListener implements StatusListener {

  @Override
  public abstract void onStatus(Status status);

  @Override
  public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

  }

  @Override
  public void onTrackLimitationNotice(int i) {

  }

  @Override
  public void onScrubGeo(long l, long l1) {

  }

  @Override
  public void onStallWarning(StallWarning stallWarning) {

  }

  @Override
  public abstract void onException(Exception e);

}
