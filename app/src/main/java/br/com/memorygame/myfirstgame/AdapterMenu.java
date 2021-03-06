package br.com.memorygame.myfirstgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by debo_ on 29/05/2016.
 */
public class AdapterMenu extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Integer> mResources = new ArrayList<Integer>();

    public AdapterMenu(Context context, List<Integer> mResources) {
        //seta a lista de imagens
        this.mResources = mResources;
        // Objeto responsável por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return mResources.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (view == null) {

            //infla o layout para podermos pegar as views
            view = mInflater.inflate(R.layout.menu_item, null);
            //cria um item de suporte para não precisarmos sempre/ inflar as mesmas informacoes
            itemHolder = new ItemSuporte();
            itemHolder.imageMenu = ((ImageView) view.findViewById(R.id.imageMenu));


            //define os itens na view;
            view.setTag(itemHolder);
        } else {
            //se a view já existe pega os itens.
            itemHolder = (ItemSuporte) view.getTag();
        }
        Picasso.with(view.getContext())
                .load(mResources.get(position))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .noFade().resize(400, 700)
                .centerCrop()
                .into(itemHolder.imageMenu);

        //retorna a view com as imagens
        return view;
    }

    /**
     * Classe de suporte para os itens do layout.
     */
    private class ItemSuporte {
        ImageView imageMenu;

    }
}
