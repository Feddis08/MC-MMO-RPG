package at.feddis08.mmorpg.minecraft.tools.classes;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Book {
    public ItemStack book;
    public BookMeta meta;
    public String title = "";
    public String display_name = "";
    public String author = "";

    public Book(String title, String display_name, String author){
        this.title = title;
        this.display_name = display_name;
        this.author = author;
    }
    public void create(){
        meta.setTitle(this.title);
        meta.setDisplayName(this.display_name);
        meta.setAuthor(author);
        book.setItemMeta(meta);
    }
}
