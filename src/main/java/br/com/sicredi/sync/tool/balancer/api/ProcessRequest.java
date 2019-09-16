package br.com.sicredi.sync.tool.balancer.api;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Guarda informações de um pedido de execucao.
 *
 * @param <Request>
 *            Requisição a ser processada.
 *
 * @author dalpiaz
 */
public class ProcessRequest<Request> implements Externalizable {

    private Request request;
    private Date timestamp;

    public ProcessRequest() {
        // Used for serialization
    }

    public ProcessRequest(Request request, Date timestamp) {
        this.request = request;
        this.timestamp = timestamp;
    }

    public Request getRequest() {
        return request;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(request);
        objectOutput.writeLong(timestamp.getTime());
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.request = (Request) objectInput.readObject();
        this.timestamp = new Date(objectInput.readLong());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ProcessRequest) {
            ProcessRequest<Request> that = (ProcessRequest<Request>) obj;

            return (this.request.equals(that.request));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.request);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("ProcessId", this.request) //$NON-NLS-1$
                .append("LastRequestDate", this.timestamp) //$NON-NLS-1$
                .toString();
    }
}
