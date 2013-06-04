public class IngressItem{
    public String name;
    public int quantity;
    
    public IngressItem(){
        this.name = new String();
        this.quantity = 1;
    }
    
    public IngressItem(String name){
        this.name = new String(name);
        this.quantity = 1;
    }
    
    public String toString(){
        String ret = Integer.toString(quantity) + "x " + name;
        return ret;
    }
}
