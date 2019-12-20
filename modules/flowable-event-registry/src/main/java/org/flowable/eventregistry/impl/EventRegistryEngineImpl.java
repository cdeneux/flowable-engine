/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.eventregistry.impl;

import org.flowable.eventregistry.api.EventRegistry;
import org.flowable.eventregistry.api.EventRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tijs Rademakers
 */
public class EventRegistryEngineImpl implements EventRegistryEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRegistryEngineImpl.class);

    protected String name;
    protected EventRepositoryService repositoryService;
    protected EventRegistry eventRegistry;
    protected EventRegistryEngineConfiguration engineConfiguration;

    public EventRegistryEngineImpl(EventRegistryEngineConfiguration engineConfiguration) {
        this.engineConfiguration = engineConfiguration;
        this.name = engineConfiguration.getEngineName();
        this.repositoryService = engineConfiguration.getEventRepositoryService();
        this.eventRegistry = engineConfiguration.getEventRegistry();
        
        if (engineConfiguration.getSchemaManagementCmd() != null) {
            engineConfiguration.getCommandExecutor().execute(engineConfiguration.getSchemaCommandConfig(), engineConfiguration.getSchemaManagementCmd());
        }

        if (name == null) {
            LOGGER.info("default flowable EventRegistryEngine created");
        } else {
            LOGGER.info("EventRegistryEngine {} created", name);
        }

        EventRegistryEngines.registerEventRegistryEngine(this);
    }

    @Override
    public void close() {
        EventRegistryEngines.unregister(this);
        engineConfiguration.close();
    }

    // getters and setters
    // //////////////////////////////////////////////////////

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EventRepositoryService getEventRepositoryService() {
        return repositoryService;
    }
    
    @Override
    public EventRegistry getEventRegistry() {
        return eventRegistry;
    }

    @Override
    public EventRegistryEngineConfiguration getEventRegistryEngineConfiguration() {
        return engineConfiguration;
    }
}