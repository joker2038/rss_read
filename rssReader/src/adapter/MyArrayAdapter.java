package adapter;

import ru.joker2038.rssreader.R;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] names;
    private final String size;
    private final String color;
    

    public MyArrayAdapter(Activity context, String[] names, String size, String color)
    {
        super(context, R.layout.adapter, names);
        this.context = context;
        this.names = names;
        this.size = size;
        this.color = color;
    }

    // Класс для сохранения во внешний класс и для ограничения доступа
    // из потомков класса
    static class ViewHolder 
    {
        public TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        // ViewHolder буферизирует оценку различных полей шаблона элемента
        ViewHolder holder;
        // Очищает сущетсвующий шаблон, если параметр задан
        // Работает только если базовый шаблон для всех классов один и тот же
        View rowView = convertView;
        if (rowView == null) 
        {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.adapter, null, true);
            holder = new ViewHolder();
            holder.textView = (TextView) rowView.findViewById(R.id.adapter);
            holder.textView.setTextSize(Float.parseFloat(size));
            holder.textView.setTextColor(Color.parseColor(color));	
            rowView.setTag(holder);
        } 
        else 
        {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.textView.setText(names[position]);
        
        return rowView;
    }
}