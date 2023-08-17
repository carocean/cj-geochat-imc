package cj.geochat.imc.master;

public interface INodeEndpointContainer {
    void add(INodeEndpoint nodeEndpoint);

    void remove(INodeEndpoint nodeEndpoint);
    String selectNodeName(int factor);

}
