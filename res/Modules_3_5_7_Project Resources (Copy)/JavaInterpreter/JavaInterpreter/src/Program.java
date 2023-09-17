public class Program
{
    private Block blk;

    public Program(Block blk)
    {
        if (blk == null)
            throw new IllegalArgumentException ("null block argument");
        this.blk = blk;
    }

    public void execute ()
    {
        blk.execute();
    }

}
