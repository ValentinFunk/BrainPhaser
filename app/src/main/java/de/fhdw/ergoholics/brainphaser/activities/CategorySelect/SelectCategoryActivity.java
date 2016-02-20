package de.fhdw.ergoholics.brainphaser.activities.CategorySelect;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;

import java.util.Arrays;
import java.util.List;

import de.fhdw.ergoholics.brainphaser.R;
import de.fhdw.ergoholics.brainphaser.model.Category;

/**
 * Created by funkv on 17.02.2016.
 */
public class SelectCategoryActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        // Set orientation to horizontal
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        // get 200dpi in px
        float cardWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300.f, getResources().getDisplayMetrics());
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        float cardHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300.f, getResources().getDisplayMetrics());

        int spans = isLandscape ? (int)Math.floor(getResources().getDisplayMetrics().heightPixels / cardHeight) : (int)Math.floor(getResources().getDisplayMetrics().widthPixels / cardWidth);
        int orientation = isLandscape ? StaggeredGridLayoutManager.HORIZONTAL : StaggeredGridLayoutManager.VERTICAL;

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spans, orientation);
        recyclerView.setLayoutManager(layoutManager);

        // categoires = CategoryDatasource.getAll( )
        List<Category> categories = Arrays.asList(
            new Category(-1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch"),
            new Category(-1l, "Architektur", "Verbessere dein Wissen über berühmte Gebäude, Bauarten und Architekturepochen..", "@drawable/architektur"),
            new Category(-1l, "Computer", "Lerne neue, coole Fakten über Computer und Informationstechnologie. Du wirst mit Fragen zu Netzwerken, Hardware, Software, Programmiersprachen und Softwareprojekten getestet.", "@drawable/computer"),
            new Category(-1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch"),
            new Category(-1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch"),
            new Category(-1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch"),
            new Category(-1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch"),
            new Category(-1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch"),
            new Category(-1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch"),
            new Category(-1l, "Englisch", "Verbessere deine Englischkenntnisse und dein Wissen über Englischsprachige Länder. Lerne nützliche Phrasen und Umgangsformen.", "@drawable/englisch")
        );

        recyclerView.setAdapter(new CategoryAdapter(categories));
    }
}
