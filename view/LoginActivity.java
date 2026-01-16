package view;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import view.databinding.ActivityLoginBinding;
import viewmodel.LoginViewModel;
import viewmodel.NavigationViewModel;
import viewmodel.ViewModelFactory;

public class LoginActivity extends Activity {

    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;
    private NavigationViewModel navigationViewModel;
    private final Observable.OnPropertyChangedCallback navigateCallback =
        new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.getNavigateToMain().get()) {
                    navigationViewModel.navigate(MainActivity.class);
                    viewModel.onNavigateHandled();
                }
            }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        setupDataBinding();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) {
            viewModel.getNavigateToMain().removeOnPropertyChangedCallback(navigateCallback);
        }
    }

    private void setupViewModel() {
        viewModel = ViewModelFactory.createLoginViewModel(getApplicationContext());
        navigationViewModel = ViewModelFactory.createNavigationViewModel(this);
    }

    private void setupDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel(viewModel);
        viewModel.getNavigateToMain().addOnPropertyChangedCallback(navigateCallback);
    }
}
