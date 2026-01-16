package viewmodel;

import android.app.Activity;
import android.content.Context;
import model.login.LoginRepository;
import model.login.LoginUseCase;
import model.login.src.LoginRepositoryImpl;
import model.login.src.LoginRemoteDataSource;
import model.login.src.UserLocalDataSource;

public class ViewModelFactory {
    public static LoginViewModel createLoginViewModel(Context context) {
        UserLocalDataSource localDataSource = new UserLocalDataSource(context);
        LoginRemoteDataSource remoteDataSource = new LoginRemoteDataSource();
        LoginRepository repository = new LoginRepositoryImpl(remoteDataSource, localDataSource);
        LoginUseCase loginUseCase = new LoginUseCase(repository);
        
        return new LoginViewModel(loginUseCase);
    }

    public static NavigationViewModel createNavigationViewModel(Activity activity) {
        return new NavigationViewModel(activity);
    }
}
