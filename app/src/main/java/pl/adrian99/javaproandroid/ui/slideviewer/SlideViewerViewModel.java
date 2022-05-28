package pl.adrian99.javaproandroid.ui.slideviewer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlideViewerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SlideViewerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}