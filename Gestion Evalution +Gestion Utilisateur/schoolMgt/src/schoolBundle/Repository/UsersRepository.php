<?php

namespace schoolBundle\Repository;

/**
 * NoteRepository
 *
 * This class was generated by the Doctrine ORM. Add your own custom
 * repository methods below.
 */
class UsersRepository extends \Doctrine\ORM\EntityRepository
{
    /**
 * @param string $role
 *
 * @return array
 */
    public function findByRole($role)
    {
        $qb = $this->_em->createQueryBuilder();
        $qb->select('u')
            ->from('schoolBundle:Users', 'u')
            ->where('u.roles LIKE :roles')->andWhere('u.classeetd IS NULL')
            ->setParameter('roles', '%"'.$role.'"%');

        return $qb->getQuery()->getResult();
    }
    public function findByRoleNot($role)
    {
        $qb = $this->_em->createQueryBuilder();
        $qb->select('u')
            ->from('schoolBundle:Users', 'u')
            ->where('u.roles LIKE :roles')
            ->setParameter('roles', '%"'.$role.'"%');

        return $qb->getQuery()->getResult();
    }
}
