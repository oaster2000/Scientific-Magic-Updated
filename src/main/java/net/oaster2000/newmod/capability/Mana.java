package net.oaster2000.newmod.capability;

public class Mana implements IMana

{

	private float mana = 250.0F;

	public void consume(float points)

	{

		this.mana -= points;

		if (this.mana < 0.0F)
			this.mana = 0.0F;

	}

	public void fill(float points)

	{

		this.mana += points;

	}

	public void set(float points)

	{

		this.mana = points;

	}

	public float getMana()

	{

		return this.mana;

	}

}