package com.ejemplo.appdocente.Util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.ejemplo.appdocente.Constants.ConstantTipoUsuario;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.R;
import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;

import java.lang.ref.WeakReference;

import androidx.fragment.app.FragmentActivity;

public class UBubbleShowCase {

    private Context context;
    private View view, showView;
    private FragmentActivity fragmentActivity;
    private BubbleShowCaseBuilder firstShowCaseBuilder, secondShowCaseBuilder, thirdShowCaseBuilder,
            fourthShowCaseBuilder, fifthShowCaseBuilder;
    private PrefManager pref;
    private String title_first, desc_first, desc_second, desc_fourth;

    public UBubbleShowCase(Context context, final WeakReference<FragmentActivity> mainReference, final View currentView ) {
        this.context = context;
        this.fragmentActivity = mainReference.get();
        this.view = currentView;
        pref = new PrefManager(mainReference.get());
    }

    public void initBuilders() {

        if (pref.getTypeUser().equals(ConstantTipoUsuario.DOCENTE)) {
            title_first = fragmentActivity.getResources().getString(R.string.show_first_title_teacher);
            desc_first = fragmentActivity.getResources().getString(R.string.show_first_desc_teacher);
            desc_second = fragmentActivity.getResources().getString(R.string.show_second_desc_teacher);
            desc_fourth = fragmentActivity.getResources().getString(R.string.show_fourth_desc_teacher);
            showView = view.findViewById(R.id.tit_view_dash_teacher);
        } else if (pref.getTypeUser().equals(ConstantTipoUsuario.ESTUDIANTE)) {
            title_first = fragmentActivity.getResources().getString(R.string.show_first_title_student);
            desc_first = fragmentActivity.getResources().getString(R.string.show_first_desc_student);
            desc_second = fragmentActivity.getResources().getString(R.string.show_second_desc_student);
            desc_fourth = fragmentActivity.getResources().getString(R.string.show_fourth_desc_student);
            showView = view.findViewById(R.id.lin_search_box);
        }

        firstShowCaseBuilder = new BubbleShowCaseBuilder(fragmentActivity) 
                .title(title_first)
                .description(desc_first)
                .backgroundColor(fragmentActivity.getResources().getColor(R.color.dot_light_screen7)) 
                .textColor(Color.BLACK)
                .titleTextSize(17) //Title text size in SP (default value 16sp)
                .descriptionTextSize(15) //Subtitle text size in SP (default value 14sp)
                .arrowPosition(BubbleShowCase.ArrowPosition.TOP) 
                .targetView(showView);
        
        

        secondShowCaseBuilder = new BubbleShowCaseBuilder(fragmentActivity) 
                .title(fragmentActivity.getResources().getString(R.string.show_second_title))
                .description(desc_second)
                .backgroundColor(fragmentActivity.getResources().getColor(R.color.dot_light_screen6)) 
                .textColor(Color.BLACK)
                .titleTextSize(17) //Title text size in SP (default value 16sp)
                .descriptionTextSize(15) //Subtitle text size in SP (default value 14sp)
                .arrowPosition(BubbleShowCase.ArrowPosition.LEFT) 
                .targetView(fragmentActivity.findViewById(R.id.bbn_item1)); 
                
                 

        thirdShowCaseBuilder = new BubbleShowCaseBuilder(fragmentActivity) 
                .title(fragmentActivity.getResources().getString(R.string.show_third_title))
                .description(fragmentActivity.getResources().getString(R.string.show_third_desc))
                .backgroundColor(fragmentActivity.getResources().getColor(R.color.dot_light_screen5)) 
                .textColor(Color.BLACK)
                .titleTextSize(17) //Title text size in SP (default value 16sp)
                .descriptionTextSize(15) //Subtitle text size in SP (default value 14sp)
                .arrowPosition(BubbleShowCase.ArrowPosition.BOTTOM) 
                .targetView(fragmentActivity.findViewById(R.id.bbn_item2)); 
        
        

        fourthShowCaseBuilder = new BubbleShowCaseBuilder(fragmentActivity) 
                .title(fragmentActivity.getResources().getString(R.string.show_fourth_title))
                .description(desc_fourth)
                .backgroundColor(fragmentActivity.getResources().getColor(R.color.dot_light_screen4)) 
                .textColor(Color.BLACK)
                .titleTextSize(17) //Title text size in SP (default value 16sp)
                .descriptionTextSize(15) //Subtitle text size in SP (default value 14sp)
                .arrowPosition(BubbleShowCase.ArrowPosition.BOTTOM) 
                .targetView(fragmentActivity.findViewById(R.id.bbn_item4)); 
        
        

        fifthShowCaseBuilder = new BubbleShowCaseBuilder(fragmentActivity) 
                .title(fragmentActivity.getResources().getString(R.string.show_fifth_title))
                .description(fragmentActivity.getResources().getString(R.string.show_fifth_desc))
                .backgroundColor(fragmentActivity.getResources().getColor(R.color.dot_light_screen3)) 
                .textColor(Color.BLACK)
                .titleTextSize(17) //Title text size in SP (default value 16sp)
                .descriptionTextSize(15) //Subtitle text size in SP (default value 14sp)
                .arrowPosition(BubbleShowCase.ArrowPosition.RIGHT) 
                .targetView(fragmentActivity.findViewById(R.id.bbn_item5)); 
        
        
    }

    public void startShowCase() {
        initBuilders();
        new BubbleShowCaseSequence()
                .addShowCase(firstShowCaseBuilder) 
                .addShowCase(secondShowCaseBuilder) 
                .addShowCase(thirdShowCaseBuilder) 
                .addShowCase(fourthShowCaseBuilder) 
                .addShowCase(fifthShowCaseBuilder) 
                .show();
    }
}
