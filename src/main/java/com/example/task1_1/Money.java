package com.example.task1_1;

public final class Money {
  private final long amount;
  private final Currency currency;

  public Money(long amount, Currency currency){
    this.amount = amount;
    this.currency = currency;
  }

  public long amount() {
    return amount;
  }
  public Currency currency() {
    return currency;
  }

  @Override
  public boolean equals(Object o){
    if (!(o instanceof Money)) return false;
    if(this == o) return true;

    long amount = ((Money) o).amount;
    Currency currency = ((Money) o).currency;

    return ((this.amount == amount) && (this.currency == currency));
  }

  /*
   Неправильная реализация hashCode() путём выбора рандомного значения хэша.
   Это решение нарушает контракт hashCode, так как:
   - Во-первых, оно не обеспечивает возврат одного и того же числа при многократном вызове в рантайме.
   - Во-вторых, для равных по equals объектов оно возвращает разные хэш-коды.

  @Override
  public int hashCode(){
    return new java.util.Random().nextInt();
  }
  */

  @Override
  public int hashCode(){
    int result = currency == null ? 0 : currency.hashCode();
    result = 31 * result + Long.hashCode(amount);

    return result;
  }

  @Override
  public String toString() {
    return String.format("%d.%02d %s", amount / 100, Math.abs(amount % 100), currency);
  }
}