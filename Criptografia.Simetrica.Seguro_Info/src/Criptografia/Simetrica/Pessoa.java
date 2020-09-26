package Criptografia.Simetrica;

public class Pessoa {
	private String id;
	private String chaveMestre;
	
	public Pessoa(String id, String chaveMestre)
	{
		this.id = id;
		this.chaveMestre = chaveMestre;
	}
	
	public String getID()
	{
		return this.id;
	}
	
	public String getChaveMestre()
	{
		return this.chaveMestre;
	}
}
