package factory.registry;

public interface AdapterService<T> {
	public void process(T request);
}