package viewmodel;

public class ObservableValue<T> {
  public interface Observer<T> {
    void onChanged(T value);
  }

  private Observer<T> observer;
  private T value;

  public void observe(Observer<T> observer) {
    this.observer = observer;
    if (value != null) {
      observer.onChanged(value);
    }
  }

  public void setValue(T value) {
    this.value = value;
    if (observer != null) {
      observer.onChanged(value);
    }
  }

  public T getValue() {
    return value;
  }
}
