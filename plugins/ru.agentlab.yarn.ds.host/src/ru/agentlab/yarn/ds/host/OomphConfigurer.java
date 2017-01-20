/**
 *
 */
package ru.agentlab.yarn.ds.host;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 *
 * Copied from <code>HelloConfigurer</code>
 *
 * @author Prihodko
 *
 */
@Component(enabled = true, immediate = true)
public class OomphConfigurer
    implements ManagedService {

    @Reference
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void bindCm(ConfigurationAdmin configAdmin) throws IOException {
        if (configAdmin != null)
        {
            Configuration config =
                configAdmin.getConfiguration("com.bmstu.coursework.oomph.ds.host.OomphComponent", null); //$NON-NLS-1$

            Dictionary props = config.getProperties();
            if (props == null)
            {
                props = new Hashtable();
            }
            // configure the Dictionary
            props.put("key", "keyValue"); //$NON-NLS-1$//$NON-NLS-2$

            // push the configuration dictionary to the SmsService
            config.update(props);
        }
    }

    public void unbindCm(ConfigurationAdmin configAdmin) {
        // Does nothing
    }

    @Activate
    public void activate(ComponentContext context) {
        System.out.println("OomphConfigurer service started"); //$NON-NLS-1$
    }

    @Deactivate
    public void deactivate(ComponentContext context) {
        System.out.println("OomphConfigurer service stopped"); //$NON-NLS-1$
    }

    @Modified
    public void modify() {
        System.out.println("OomphConfigurer service modified"); //$NON-NLS-1$
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        // Does nothing
    }

}
