package viewmodel.mock;

import viewmodel.ObservableValue;

public class TestObserver<T> implements ObservableValue.Observer<T> {
  private T lastValue;
  private int changeCount;

  @Override
  public void onChanged(T value) {
    lastValue = value;
    changeCount += 1;
  }

  public T getLastValue() {
    return lastValue;
  }

  public int getChangeCount() {
    return changeCount;
  }
}
