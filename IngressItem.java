public class IngressItem{
    public String name;
    public int quantity;
    
    public IngressItem(){
        this.name = new String();
    }
    
    public IngressItem(String name){
        this.name = new String(name);
    }
    
    public String toString(){
        String ret = name;
        return name;
    }
}
