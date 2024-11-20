package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RequiredArgsConstructor
public abstract class PositionableService<T, ID> {
    protected final JpaRepository<T, ID> repository;

    protected abstract int getPosition(T entity);

    protected abstract void setPosition(T entity, int position);

    protected abstract List<T> findAllHigherOrEqualPosition(long parentId, int position);

    protected abstract List<T> findAllHigherPosition(long parentId, int position);

    protected abstract List<T> findAllByParentId(long parentId);

    public T create(T entity, long parentId, int position) {
        List<T> higherEntities = findAllHigherOrEqualPosition(parentId, position);

        for (T e : higherEntities) {
            setPosition(e, getPosition(e) + 1);
        }
        repository.saveAll(higherEntities);

        setPosition(entity, position);
        return repository.save(entity);
    }

    public T update(T entity, int newPosition, long parentId) {
        int currentPosition = getPosition(entity);
        int delta = currentPosition - newPosition;

        if (delta != 0) {
            List<T> relatedEntities = findAllByParentId(parentId);

            for (T e : relatedEntities) {
                int pos = getPosition(e);

                if (e.equals(entity)) {
                    setPosition(entity, newPosition);
                }

                if (delta < 0 && pos > currentPosition && pos <= newPosition) {
                    setPosition(e, pos - 1);
                } else if (delta > 0 && pos >= newPosition && pos < currentPosition) {
                    setPosition(e, pos + 1);
                }
            }
            repository.saveAll(relatedEntities);
        }

        return repository.save(entity);
    }

    public void delete(T entity, long parentId) {
        int position = getPosition(entity);
        List<T> higherEntities = findAllHigherPosition(parentId, position);

        for (T e : higherEntities) {
            setPosition(e, getPosition(e) - 1);
        }

        repository.delete(entity);
        repository.saveAll(higherEntities);
    }
}
